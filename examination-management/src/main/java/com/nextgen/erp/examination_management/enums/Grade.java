package com.nextgen.erp.examination_management.enums;

public enum Grade {

    A_PLUS,
    A,
    B_PLUS,
    B,
    C,
    D,
    F;

    public static Grade fromPercentage(double percentage) {

        if (percentage >= 90) return A_PLUS;
        if (percentage >= 80) return A;
        if (percentage >= 70) return B_PLUS;
        if (percentage >= 60) return B;
        if (percentage >= 50) return C;
        if (percentage >= 40) return D;

        return F;
    }
}
