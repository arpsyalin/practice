package com.lyl.butterknife.compiler;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

public abstract class BuildObject {
    protected Class clazz;
    protected Element element;
    protected List<Statement> statements = new ArrayList<>();

    public BuildObject(Class clazz, Element element) {
        this.element = element;
        this.clazz = clazz;
        initStatement();
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public List<Statement> getStatement() {
        return statements;
    }

    protected abstract void initStatement();
}
