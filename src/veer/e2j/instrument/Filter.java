package veer.e2j.instrument;

import veer.e2j.collect.ClassDefinition;
import veer.e2j.collect.Constraint;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;

import java.security.ProtectionDomain;

import java.util.Collection;

import java.util.Queue;

public class Filter implements ClassFileTransformer {

  private final Collection<Constraint> constraints;
  private final Queue<ClassDefinition> sink;

  public Filter(Collection<Constraint> constraints, Queue<ClassDefinition> sink) {
    this.constraints = constraints;
    this.sink = sink;
  }

  public byte[] transform(ClassLoader loader, String name, Class<?> clazz,
                          ProtectionDomain domain, byte[] data)
      throws IllegalClassFormatException {
    boolean accepted = true;
    for (Constraint constraint : constraints) {
      accepted = accepted && constraint.accept(loader, name);
    }
    if (accepted) {
      // 为了防止一些异常名字的class，这里给类名字加一个随机数
      sink.offer(new ClassDefinition(name + Math.random(), data));
    }
    return data;
  }
}
