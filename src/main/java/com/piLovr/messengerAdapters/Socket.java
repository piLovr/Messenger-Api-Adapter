package com.piLovr.messengerAdapters;

import lombok.Getter;

@Getter
public abstract class Socket implements UnifiedSocket {
    protected String alias;
    protected boolean connected = false;
}
