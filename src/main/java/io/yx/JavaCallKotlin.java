package io.yx;

/**
 * @author YX
 * @date 2023/6/7 17:32
 */
public class JavaCallKotlin {

    /**
     * 尝试从java调用kotlin
     *
     * @param args
     */
    public static void main(String[] args) {
        KtMain ktMain = new KtMain();
        ktMain.main(args);

        TestKt.myFun();
    }
}
