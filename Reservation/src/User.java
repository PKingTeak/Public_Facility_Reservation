public class User
{
    private static long userCount = 0; //고유번호로 존재해야하기 때문에
    private long id; 
    private String name;
    private int age;

    public User(String _name, int _age)
    {
        userCount++;
        this.id++;
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