package com.alei.chapter11;

/**
 * @author LeiLiMin
 * @date: 2022/11/17
 */
public class T01_PointerThis {
    public static void main(String[] args) {
        PointerThis pointerThis = new PointerThis();
        // 使用.new语法来创建内部类
        PointerThis.InnerClass innerClass = pointerThis.new InnerClass();

        // 为什么如下方式不行,因为非static内部类,实例化的时候需要与外围对象建立关联,所以必须得使用外围对象来实例话内部对象
        // PointerThis.InnerClass innerClass0= new PointerThis.InnerClass();
    }


}

class PointerThis {
    private int index;

    private void showInteger(Integer param) {
        System.out.println("Param int = " + param + "\t index = " + index);
    }


    /**
     * 内部类可以调用外围类的任何属性和行为
     */
    class InnerClass {
        public void invokePointThis() {
            // 直接使用外围对象的属性和方法是可以的
            // PointerThis.this: 使用外围对象名.this对象也可以
            index = 10;
            PointerThis.this.showInteger(1);
        }

        public InnerClass() {
        }
    }

    public InnerClass makeInnerClass() {
        return new InnerClass();
    }
}
