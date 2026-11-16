
package model;

import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;

@DatabaseTable(tableName = "Player")
public class Player {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "NickName")
    private String nickName;

    @DatabaseField
    private int forca;

    @DatabaseField
    private int hp;

    public Player() {
    }

    public Player(String nickName, int forca, int hp) {
        this.nickName = nickName;
        this.forca = forca;
        this.hp = hp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getForca() {
        return forca;
    }

    public void setForca(int forca) {
        this.forca = forca;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
