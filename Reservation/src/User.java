public class User
{
    static long totalUserCounter = 0;
    private long id; 
    private String name;
    private int age;
    //성별
    

    public User(String _name, int _age)
    {
        totalUserCounter++;
        this.id = totalUserCounter;
        this.name = _name;
        this.age = _age;
    }
    
    public Long getUserId()
    {
        return this.id;
    }
    public String getName()
    {
        return this.name;
    }

    public int getAge()
    {
        return this.age;

    }

    public long getId()
    {
        return this.id;
    }

}