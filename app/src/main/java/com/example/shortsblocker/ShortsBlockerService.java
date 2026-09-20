package com.example.shortsblocker;

import android.view.accessibility.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.content.Context;

public class ShortsBlockerService extends AccessibilityService {
    private int viewCount = 0;
    private boolean insideShortsFeed = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode == null) return;

        boolean limitEnabled = getSharedPreferences("BlockerPrefs", Context.MODE_PRIVATE)
                                .getBoolean("ALLOW_FIRST_SHORT", true);
        boolean shortsActive = checkIsShortsActive(rootNode);

        if (shortsActive) {
            if (!insideShortsFeed) {
                insideShortsFeed = true;
                viewCount++;
            }
            if (limitEnabled && viewCount > 1) {
                performGlobalAction(GLOBAL_ACTION_BACK);
            }
        } else {
            insideShortsFeed = false;
            viewCount = 0;
        }
    }

    private boolean checkIsShortsActive(AccessibilityNodeInfo node) {
        if (node == null) return false;
        
        CharSequence id = node.getViewIdResourceName();
        CharSequence desc = node.getContentDescription();
        
        if ((id != null && id.toString().contains("shorts")) || 
            (desc != null && desc.toString().toLowerCase().contains("shorts"))) {
            return true;
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            if (checkIsShortsActive(node.getChild(i))) return true;
        }
        return false;
    }

    @Override public void onInterrupt() {}
}
