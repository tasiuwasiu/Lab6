package lab.wasikrafal.lab6;

import java.util.List;

/**
 * Created by Rafał on 27.04.2018.
 */

public interface GetResponse
{
    void processReceiving(Boolean isReceived, List<String> messages);
}
