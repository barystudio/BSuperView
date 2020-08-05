package com.bary.ui.view.eum;

public enum GradientOrientation {
        HORIZONTAL(1),//水平
        VERTICAL(2),//垂直
        DIAGONAL(4);//对角
        int id;
        GradientOrientation(int id) {
            this.id = id;
        }
        public static GradientOrientation fromId(int id) {
            for (GradientOrientation f : values()) {
                if (f.id == id) return f;
            }
            throw new IllegalArgumentException();
        }
    }