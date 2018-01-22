import javax.persistence.*;

@Entity
@Table(name = "ACCOUNTS")
public class Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer amount;

    @OneToOne(mappedBy = "account")
    private User user;

    public Account() { }

    public Account(Integer amount) {
        this.amount = amount;
    }

    public void processMoney(Integer money) throws Exception {
        Integer newAmount = amount + money;
        if (newAmount < 0) {
            throw new Exception("NOT ENOUGHT MONEY!!!\nCurrent amount is " + amount + ". Requested amount is " + money);
        }
        else {
            this.amount = newAmount;
        }
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getAmount() {
        return amount;
    }
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
