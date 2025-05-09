package ru.imagebook.client.common.util;

public class YaMetrikaUtils {
    private YaMetrikaUtils() {
    }

    public static void lkRegistractionGoal() {
        reachGoal("lk-registration");
    }

    public static void editorRegistractionGoal() {
        reachGoal("editor-registration");
    }

    public static void statusRequestGoal() {
        reachGoal("status-request");
    }

    public static native void reachGoal(String goal)/*-{
        $wnd.yaCounter1591621.reachGoal(goal);
    }-*/;
}
