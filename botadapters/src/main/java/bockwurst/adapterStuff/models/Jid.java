package bockwurst.adapterStuff.models;

import it.auties.whatsapp.model.jid.JidServer;

import java.util.List;
import java.util.Objects;

public class Jid {
    private String user;
    private JidServer server;
    private Integer device;
    private Integer agent;
    private it.auties.whatsapp.model.jid.Jid jid;

    public Jid(String user, JidServer server, Integer device, Integer agent) {
        this.user = user;
        this.server = server;
        this.device = device;
        this.agent = agent;
        this.jid = new it.auties.whatsapp.model.jid.Jid(user, server, device, agent);
    }

    public Jid(String user, String server, Integer device, Integer agent) {
        this.user = user;
        this.server = JidServer.of(server);
        this.device = device;
        this.agent = agent;
        this.jid = new it.auties.whatsapp.model.jid.Jid(user, this.server, device, agent);
    }

    public Jid(it.auties.whatsapp.model.jid.Jid jid) {
        this.user = jid.user();
        this.server = jid.server();
        this.device = jid.device();
        this.agent = jid.agent();
        this.jid = jid;
    }

    public Jid(String jid) {
        this.user = jid.split("@")[0];
        if(jid.contains("@")) {
            this.server = JidServer.of(jid.split("@")[1]);
        } else {
            System.out.println("Jid: " + jid);
            this.server = JidServer.of(jid);
        }
        this.device = null;
        this.agent = null;
        this.jid = new it.auties.whatsapp.model.jid.Jid(user, server, device, agent);
    }
    public it.auties.whatsapp.model.jid.Jid getJid(){
        return jid;
    }
    public String toString(){
        return user+"@"+server.toString();
    }
    public String toMention() {
        return "@"+user;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jid jid1 = (Jid) o;
        return user.equals(jid1.user) && server.equals(jid1.server);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, server);
    }

    public static String toMentionsLine(List<Jid> jids){
        String mentionsOneLine = jids.stream().map(Jid::toMention).reduce("", (a, b) -> a + ", " + b);
        if(mentionsOneLine.contains(",")) {
            mentionsOneLine = mentionsOneLine.substring(1, mentionsOneLine.lastIndexOf(",")) + " &" + mentionsOneLine.substring(mentionsOneLine.lastIndexOf(",") + 1);
        }//todo fixxen
        return mentionsOneLine;
    }
    public static String toMentionsList(List<Jid> jids){
        String mentionsList = "";
        for(int i = 0, size = jids.size(); i < size; i++){
            mentionsList += jids.get(i).toMention();
            if(i < size - 1){
                mentionsList += "\n";
            }
        }
        return mentionsList;
    }
}
