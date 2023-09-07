package com.digitalfactory.bookstore.web.response;

import lombok.Data;

@Data
public class EntityId<T> {

    private T id;

    public EntityId() {
        //
    }
}
