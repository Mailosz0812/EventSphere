package org.locations.eventsphere.mappers;

import java.util.List;

public interface Mapper<A,B> {
    A mapFrom(B b);
    B mapTo(A a);
    List<A> mapFromList(List<B> bList);
    List<B> mapToList(List<A> aList);
}
