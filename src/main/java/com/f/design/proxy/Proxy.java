package com.f.design.proxy;

public class Proxy implements Subject {

    private RealSubject subject;

    public Proxy() {
        subject = new RealSubject();
    }

    @Override
    public void doSomeThing() {
        subject.doSomeThing();
    }
}