package com.example.clippy;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

public class ClipboardService extends TileService {
    ClipboardManager.OnPrimaryClipChangedListener clipChangedListener = new ClipboardManager.OnPrimaryClipChangedListener() {
        @Override
        public void onPrimaryClipChanged() {
            updateTile();
        }
    };
    boolean hasListener = false;

    @Override
    public void onTileAdded() {
        super.onTileAdded();

        updateTile();
        registerListener();
    }

    @Override
    public void onClick() {
        super.onClick();

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.clearPrimaryClip();
        Toast.makeText(ClipboardService.this, "Clipboard cleared", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();

        updateTile();
        registerListener();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        unregisterListener();
    }

    private void registerListener() {
        if(!hasListener) {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.addPrimaryClipChangedListener(clipChangedListener);
            hasListener = true;
        }
    }

    private void unregisterListener() {
        if(hasListener) {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.removePrimaryClipChangedListener(clipChangedListener);
            hasListener = false;
        }
    }

    private void updateTile() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = clipboardManager.getPrimaryClip();

        Tile qsTile = getQsTile();
        qsTile.setState(clipData != null ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        qsTile.updateTile();
    }
}
