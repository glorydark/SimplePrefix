package glorydark.nukkit.data;

public class PlayerPrefixData {

    protected String identifier;

    protected long expireMillis;

    public PlayerPrefixData(String identifier, long expireMillis) {
        this.identifier = identifier;
        this.expireMillis = expireMillis;
    }

    public boolean isExpired() {
        if (expireMillis == -1) {
            return false;
        }
        return System.currentTimeMillis() >= expireMillis;
    }

    public String getIdentifier() {
        return identifier;
    }

    public long getExpireMillis() {
        return expireMillis;
    }
}
