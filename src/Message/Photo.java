package Message;

import Message.Message;

import java.awt.*;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/11/9 20:23
 */
public class Photo extends Message {
    private Image image;
    public Photo(String fromUser, String toUser,String imagePath, Image image) {
        super(fromUser, toUser, imagePath);
        //TODO
    }
}
