package com.alei.msb.teacherhuang;

/**
 * @Author LeiLiMin
 * @Description:
 * @date: 2022/6/9
 */
public class T05_ForkJoinPool {
    /**
     * 架构设计
     * 1.ForkJoinPool由多个线程组成
     * 2.每个线程对应一个内部队列和外部队列
     *   : 外部线程放任务存入外部队列
     *   : 内部线程放任务存入内部队列
     * 3.工作线程从自己队列头部取任务
     * 4.其他线程从其他内部线程队列的尾部窃取(CAS)窃取任务
     * 5.工作队列与内部线程互相持有引用
     */
    public static void main(String[] args) {

    }
}
