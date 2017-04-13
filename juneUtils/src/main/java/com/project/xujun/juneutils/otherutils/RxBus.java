package com.project.xujun.juneutils.otherutils;

import rx.Observable;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import rx.subjects.Subject;
import rx.subjects.PublishSubject;

/**
 * Created by nextsmile on 2016/7/29.
 */
public class RxBus {
    private static RxBus instance;
    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = null;

    public static synchronized RxBus get() {
        if (null == instance) {
            instance = new RxBus();
        }
        return instance;
    }

    private RxBus() {
        subjectMapper = new ConcurrentHashMap<>();
    }

    public <T> Observable<T> register(@NonNull Class<T> clazz) {
        List<Subject> subjectList = subjectMapper.get(clazz.getName());
        if (null == subjectList) {
            subjectList = new ArrayList<>();
            subjectMapper.put(clazz.getName(), subjectList);
        }
        Subject<T, T> subject;
        subjectList.add(subject = PublishSubject.create());
        return subject;
    }

    public void unregister(@NonNull Class<?> clazz, @NonNull Observable observable) {
        List<Subject> subjects = subjectMapper.get(clazz.getName());
        if (null != subjects) {
            subjects.remove(observable);
            if (subjects.isEmpty()) {
                subjectMapper.remove(clazz.getName());
            }
        }
    }


    public void post(@NonNull Object content) {
        List<Subject> subjectList = subjectMapper.get(content.getClass().getName());
        if (null == subjectList || subjectList.isEmpty()) {
            return;
        }
        for (Subject subject : subjectList) {
            subject.onNext(content);
        }
    }
}
