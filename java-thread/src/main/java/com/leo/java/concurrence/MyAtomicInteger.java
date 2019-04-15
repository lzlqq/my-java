package com.leo.java.concurrence;

import java.util.function.IntUnaryOperator;

import sun.misc.Unsafe;

/**
 * CAS 乐观锁
 * 
 * @author LSH7120
 *
 */
@SuppressWarnings("restriction")
public class MyAtomicInteger extends Number implements java.io.Serializable{

    private static final long serialVersionUID = 6214790243416807050L;

    // setup to use Unsafe.compareAndSwapInt for updates
    private static final Unsafe unsafe = Unsafe.getUnsafe();

    private static final long valueOffset;

    static{
        try{
            valueOffset = unsafe.objectFieldOffset(MyAtomicInteger.class.getDeclaredField("value"));
        }catch (Exception ex){
            throw new Error(ex);
        }
    }

    private volatile int value;

    /**
     * Creates a new AtomicInteger with the given initial value.
     *
     * @param initialValue
     *            the initial value
     */
    public MyAtomicInteger(int initialValue){
        value = initialValue;
    }

    /**
     * Creates a new AtomicInteger with initial value {@code 0}.
     */
    public MyAtomicInteger(){
    }

    /**
     * Gets the current value.
     *
     * @return the current value
     */
    public final int get(){
        return value;
    }

    /**
     * Sets to the given value.
     *
     * @param newValue
     *            the new value
     */
    public final void set(int newValue){
        value = newValue;
    }

    /**
     * Atomically increments by one the current value.
     *
     * @return the previous value
     */
    public final int getAndIncrement(){
        return unsafe.getAndAddInt(this, valueOffset, 1);
    }

    /**
     * Atomically sets the value to the given updated value
     * if the current value {@code ==} the expected value.
     *
     * @param expect
     *            the expected value
     * @param update
     *            the new value
     * @return {@code true} if successful. False return indicates that
     *         the actual value was not equal to the expected value.
     */
    public final boolean compareAndSet(int expect,int update){
        return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
    }

    /**
     * Atomically updates the current value with the results of
     * applying the given function, returning the previous value. The
     * function should be side-effect-free, since it may be re-applied
     * when attempted updates fail due to contention among threads.
     *
     * @param updateFunction
     *            a side-effect-free function
     * @return the previous value
     * @since 1.8
     */
    public final int getAndUpdate(IntUnaryOperator updateFunction){
        int prev, next;
        do{
            prev = get();
            next = updateFunction.applyAsInt(prev);
        }while (!compareAndSet(prev, next));
        return prev;
    }

    /**
     * Atomically updates the current value with the results of
     * applying the given function, returning the updated value. The
     * function should be side-effect-free, since it may be re-applied
     * when attempted updates fail due to contention among threads.
     *
     * @param updateFunction
     *            a side-effect-free function
     * @return the updated value
     * @since 1.8
     */
    public final int updateAndGet(IntUnaryOperator updateFunction){
        int prev, next;
        do{
            prev = get();
            next = updateFunction.applyAsInt(prev);
        }while (!compareAndSet(prev, next));
        return next;
    }

    /**
     * My get and increment
     * 
     * @return
     */
    public final int myGetAndIncrement(){
        for (;;){
            int current = get();
            int next = current + 1;
            if (compareAndSet(current, next))
                return current;
        }
    }

    @Override
    public int intValue(){
        return 0;
    }

    @Override
    public long longValue(){
        return 0;
    }

    @Override
    public float floatValue(){
        return 0;
    }

    @Override
    public double doubleValue(){
        return 0;
    }

}
