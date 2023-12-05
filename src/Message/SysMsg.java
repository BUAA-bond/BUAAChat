package Message;

import java.io.Serializable;

/**
 * Message that carry information to or from server.
 * @author WnRock
 * @version 0.1
 */
public class SysMsg extends Message implements Serializable {
    /**
     * SysMsg type
     *   0 - default Unused
     *   1 - login
     *   2 - register
     *   3 - System command
     *  -1 - quit
     * 200 - OK, current operation complete
     * 500 - error
     */
    private int type = 0;

    /**
     * Construct SysMsg.
     * @param
     * @param type
     */
    public SysMsg(String fromUser,int type){
        super(fromUser);
        this.type = type;
    }

    /**
     * Construct SysMsg.
     * @param fromUser
     * @param type
     * @param content
     */
    public SysMsg(String fromUser,int type,String content){
        super(fromUser,"0",content);
        this.type = type;
    }

    /**
     * get SysMsg type
     * @return type
     */
    public int getType(){ return this.type; }
}
