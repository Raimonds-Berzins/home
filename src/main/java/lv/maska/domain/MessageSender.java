package lv.maska.domain;

public interface MessageSender {
    public void Send(Address reciever, String message) throws Exception;
}
