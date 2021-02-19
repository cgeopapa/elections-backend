package gr.elections.sad.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
public class Device
{
    @Id
    @Column(unique = true)
    private final String code;
    private int codeTries;

    private String pass;
    private int passCount = 0;
    private short authed = 0;

    @OneToOne(cascade = CascadeType.ALL)
    private Data data;

    public Device(String code)
    {
        this.code = code;
        codeTries = 1;
    }

    public short getAuthed()
    {
        return authed;
    }

    public void setAuthed(short authed)
    {
        this.authed = authed;
    }

    public int getPassCount()
    {
        return passCount;
    }

    public void setPassCount(int passCount)
    {
        this.passCount = passCount;
    }

    public String getCode()
    {
        return code;
    }

    public int getCodeTries()
    {
        return codeTries;
    }

    public void setCodeTries(int codeTries)
    {
        this.codeTries = codeTries;
    }

    public String getPass()
    {
        return pass;
    }

    public void setPass(String pass)
    {
        this.pass = pass;
    }

    public Data getData()
    {
        return data;
    }

    public void setData(Data data)
    {
        this.data = data;
    }
}
