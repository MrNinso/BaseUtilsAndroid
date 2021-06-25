package com.developer.base.utils.android.tools.async;

import android.os.Handler;
import android.os.Looper;

import com.developer.base.utils.android.lists.BaseList;

public class BasePromise<T> {
    private final BaseList<OnAsync<?, ?>> mHandlers = new BaseList<>();
    private boolean mCancel = false;
    public T Input;

    public <R> BasePromise(OnAsync<T, R> handler) {
        this.mHandlers.add(0, handler);
    }

    public <P, R> BasePromise<T> then(OnAsync<P, R> handler) {
        this.mHandlers.add(handler);
        return this;
    }

    public void cancel() {
        this.mCancel = true;
    }

    public void resolve() {
        run();
    }

    public void resolve(boolean cancelable) {
        if (cancelable)
            runCancelable();
        else
            run();
    }

    public <P, R> void resolve(OnSync<P, R> handler) {
        this.mHandlers.add(handler);
        run();
    }

    private void run() {
        new Thread(() -> {
            final Object[] r = {this.Input};
            mHandlers.forEach((i, p) ->
                    r[0] = ((OnAsync<? super Object, ? super Object>) p).run(r[0])
            );
        }).start();
    }

    private void runCancelable() {
        new Thread(() -> {
            final Object[] r = {this.Input};
            mHandlers.forEachBreakable((i, p) -> {
                r[0] = ((OnAsync<? super Object, ? super Object>) p).run(r[0]);
                if (this.mCancel) {
                    this.mCancel = false;
                    return BaseList.EachBreakable.BREAK;
                }

                return BaseList.EachBreakable.CONTINUE;
            });
        }).start();
    }

    public interface OnAsync<T, R> {
        R run(T t);
    }

    public interface OnSync<T, R> extends OnAsync<T, R> {
        default R run(T t) {
            new Handler(Looper.getMainLooper()).post(() -> runSync(t));
            return null;
        }

        void runSync(T t);

    }
}
