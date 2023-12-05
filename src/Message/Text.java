package Message;

import java.io.Serializable;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/11/9 20:22
 */
public class Text extends Message implements Serializable {
    public Text(String formUser,String toUser,String content){
        super(formUser,toUser,content);
    }
}
