package com.bary.ui.view.eum;

public enum EditMode {
        NORMAL(1),//【普通模式】
    UNEDITABLE(2),//【不可编辑模式】
    NOKEYBOARD(4);//【可编辑不弹输入法】（有光标，一般用在语音录入）
        int id;
        EditMode(int id) {
            this.id = id;
        }
        public static EditMode fromId(int id) {
            for (EditMode f : values()) {
                if (f.id == id) return f;
            }
            throw new IllegalArgumentException();
        }
    }