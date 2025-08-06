package backup;

import java.util.Set;

public record ChatEvent(Chat chat, ChatEventType chatEventType, User actor, Set<User> affectedUsers){}
