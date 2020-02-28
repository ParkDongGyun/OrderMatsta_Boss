package com.sbsj.ordermatstaboss.Util;

public interface OnItemTouchHelpListener {
    boolean onItemMove(int fromPosition, int toPosition);

    void onItemRemove(int position);
}
