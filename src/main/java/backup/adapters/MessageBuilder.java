package backup.adapters;

import backup.attachments.Attachment;

import java.util.Arrays;
import java.util.List;

public abstract class MessageBuilder {
    protected List<Attachment> attachments;
    protected ExtendedMessage message;

    public void addAttachment(Attachment... attachment){
        List<Attachment> attachmentList = Arrays.asList(attachment);
        //Check compatible Attachment-types
    };

    public void addText(String text){
        message.text = text;
    }
}
