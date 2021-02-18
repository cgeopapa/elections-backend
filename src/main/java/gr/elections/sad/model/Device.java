package gr.elections.sad.model;

import javax.persistence.*;

@Entity
public class Device
{
    @Id
    @Column(unique = true)
    private String uid;

    private String pass;

    @OneToOne(cascade = CascadeType.ALL)
    private Data data;

    public Device(String uid)
    {
        this.uid = uid;
    }
}
