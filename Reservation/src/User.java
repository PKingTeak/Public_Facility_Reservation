public class User
{
    private long id;
    private String name;
    private int age;

    public User(long _id, String _name, int _age)
    {
        this.id = _id;
        this.name = _name;
        this.age = _age;
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

    private void changeName(String _name)
    {
        this.name = _name;
    }
    

        
}