package gr.elections.sad.model;

public class RepresentativeInfo
{
    private String fname;
    private String lname;
    private String phone;

    public RepresentativeInfo()
    {
        fname = "Αντιπρόσωπος";
        lname = "Δικαστόπουλος";
        phone = "6948059052";
    }

    public String getFname()
    {
        return fname;
    }

    public void setFname(String fname)
    {
        this.fname = fname;
    }

    public String getLname()
    {
        return lname;
    }

    public void setLname(String lname)
    {
        this.lname = lname;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }
}
