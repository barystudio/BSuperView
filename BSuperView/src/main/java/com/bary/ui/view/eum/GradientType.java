package com.bary.ui.view.eum;

public enum GradientType {
        LINEAR(1),//线性
        RADIAL(2),//圆环
        SWEEP(4);//角度
        int id;
        GradientType(int id) {
            this.id = id;
        }
        public static GradientType fromId(int id) {
            for (GradientType f : values()) {
                if (f.id == id) return f;
            }
            throw new IllegalArgumentException();
        }
    }