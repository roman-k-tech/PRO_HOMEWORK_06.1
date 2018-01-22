import javax.persistence.*;

@Entity
@Table(name = "TRANSACTIONS")
public class MoneyTransaction
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private Boolean finished;
    @Column(nullable = false)
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "from_account", nullable = false)
    Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account", nullable = false)
    Account toAccount;

    public MoneyTransaction() {}

    public MoneyTransaction(Account fromAccount, Account toAccount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    public Boolean processTransation(int amount)
    {
        this.amount = amount;
        if (amount > 0)
        {
            try {
                fromAccount.processMoney(-amount);
                toAccount.processMoney(amount);
                finished = true;
                return finished;
            }
            catch (Exception e) { System.out.println(e.getMessage()); }
        }
        else if (amount < 0)
        {
            try {
                toAccount.processMoney(amount);
                fromAccount.processMoney(-amount);
                finished = true;
                return finished;
            }
            catch (Exception e) { System.out.println(e.getMessage()); }
        }
        else if (amount == 0) {
            return finished = true;
        }
        return finished = false;
    }
    public Boolean isFinished() {return finished; }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Boolean getFinished() {
        return finished;
    }
    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
    public Account getFromAccount() {
        return fromAccount;
    }
    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }
    public Account getToAccount() {
        return toAccount;
    }
    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }
    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }
}
