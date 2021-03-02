package dataprovider;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * DummyQuery.
 *
 * @author Pierre Adam
 * @since 21.03.02
 */
public class DummyProvider {

    /**
     * The constant SAMPLE_SIZE.
     */
    public static final int SAMPLE_SIZE = 200;

    private int startIdx;

    private int numberOfElem;

    private List<PersonEntity> data;

    /**
     * Instantiates a new Dummy query.
     */
    public DummyProvider() {
        this.data = new ArrayList<>();
        final Faker faker = new Faker();

        for (int i = 0; i < DummyProvider.SAMPLE_SIZE; i++) {
            this.data.add(new PersonEntity(faker.name()));
        }
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public List<PersonEntity> getData() {
        return this.data;
    }

    /**
     * Sets pagination.
     *
     * @param startIdx     the start idx
     * @param numberOfElem the number of elem
     */
    public void setPagination(final int startIdx, final int numberOfElem) {
        this.startIdx = startIdx;
        this.numberOfElem = numberOfElem;
    }

    /**
     * Gets result.
     *
     * @return the result
     */
    public List<PersonEntity> getResult() {
        if (this.startIdx >= this.data.size()) {
            return new ArrayList<>();
        }
        final int endIdx = this.startIdx + this.numberOfElem;
        return this.data.subList(this.startIdx, Math.min(endIdx, this.data.size()));
    }

    /**
     * Gets initial size.
     *
     * @return the initial size
     */
    public int getInitialSize() {
        return DummyProvider.SAMPLE_SIZE;
    }

    /**
     * Alter data.
     *
     * @param alterCall the alter call
     */
    public void alterData(final Function<List<PersonEntity>, List<PersonEntity>> alterCall) {
        this.data = alterCall.apply(this.data);
    }
}
