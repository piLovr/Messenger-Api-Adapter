package com.piLovr.messengerAdapters.adapters;

import com.piLovr.messengerAdapters.UnifiedSocket;
import lombok.Getter;

@Getter
public abstract class Socket implements UnifiedSocket {
    protected String alias;
    protected boolean connected = false;
}
