/*
 * MIT License
 *
 * Copyright (c) 2021 Pierre Adam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package mocking.dataprovider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import mocking.dataprovider.enums.NumberEnum;
import mocking.dataprovider.enums.SimpleEnum;
import org.joda.time.DateTime;
import play.libs.Json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;
import java.util.UUID;

/**
 * PersonEntity.
 *
 * @author Pierre Adam
 * @since 21.03.02
 */
public class PersonEntity {

    /**
     * The Created at.
     */
    private final DateTime createdAt;

    /**
     * The Uid.
     */
    private final UUID uid;

    /**
     * The First name.
     */
    private final String firstName;

    /**
     * The Last name.
     */
    private final String lastName;

    /**
     * The Title.
     */
    private final String title;

    /**
     * The Blood group.
     */
    private final String bloodGroup;

    /**
     * The Is active.
     */
    private final Boolean active;

    /**
     * The Long number.
     */
    private final Long longNumber;

    /**
     * The Integer number.
     */
    private final Integer integerNumber;

    /**
     * The Double number.
     */
    private final Double doubleNumber;

    /**
     * The Float number.
     */
    private final Float floatNumber;

    /**
     * The Big integer number.
     */
    private final BigInteger bigIntegerNumber;

    /**
     * The Big decimal number.
     */
    private final BigDecimal bigDecimalNumber;

    /**
     * The Json node.
     */
    private final JsonNode jsonNode;

    /**
     * The Simple enum.
     */
    private final SimpleEnum simpleEnum;

    /**
     * The Number enum.
     */
    private final NumberEnum numberEnum;

    /**
     * The Null data.
     */
    private final String nullData;

    /**
     * The Address.
     */
    private final AddressEntity address;

    /**
     * Instantiates a new Person entity.
     */
    public PersonEntity(final Faker faker) {
        final Random random = new Random();
        final Name name = faker.name();
        this.createdAt = DateTime.now().minusSeconds(random.nextInt(3600 * 24 * 200)); // Over the last 200 days
        this.uid = UUID.randomUUID();
        this.firstName = name.firstName();
        this.lastName = name.lastName();
        this.title = name.title();
        this.bloodGroup = name.bloodGroup();
        this.active = random.nextBoolean();
        this.longNumber = random.nextLong();
        this.integerNumber = random.nextInt();
        this.doubleNumber = random.nextDouble();
        this.floatNumber = random.nextFloat();
        this.bigIntegerNumber = BigInteger.valueOf(random.nextLong());
        this.bigDecimalNumber = BigDecimal.valueOf(random.nextDouble());
        this.nullData = null;

        final ArrayNode jsonArray = Json.newArray();
        this.jsonNode = jsonArray;
        jsonArray.add(this.longNumber);
        jsonArray.add(this.integerNumber);

        this.simpleEnum = SimpleEnum.rand(random);
        this.numberEnum = NumberEnum.rand(random);

        this.address = new AddressEntity(faker);
    }

    /**
     * Gets created at.
     *
     * @return the created at
     */
    public DateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public UUID getUid() {
        return this.uid;
    }

    /**
     * Gets first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Gets last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String title() {
        return this.title;
    }

    /**
     * Gets blood group.
     *
     * @return the blood group
     */
    public String getBloodGroup() {
        return this.bloodGroup;
    }

    /**
     * Gets active.
     *
     * @return the active
     */
    public Boolean isActive() {
        return this.active;
    }

    /**
     * Gets long number.
     *
     * @return the long number
     */
    public Long getLongNumber() {
        return this.longNumber;
    }

    /**
     * Gets integer number.
     *
     * @return the integer number
     */
    public Integer getIntegerNumber() {
        return this.integerNumber;
    }

    /**
     * Gets double number.
     *
     * @return the double number
     */
    public Double getDoubleNumber() {
        return this.doubleNumber;
    }

    /**
     * Gets float number.
     *
     * @return the float number
     */
    public Float getFloatNumber() {
        return this.floatNumber;
    }

    /**
     * Gets big integer number.
     *
     * @return the big integer number
     */
    public BigInteger getBigIntegerNumber() {
        return this.bigIntegerNumber;
    }

    /**
     * Gets big decimal number.
     *
     * @return the big decimal number
     */
    public BigDecimal getBigDecimalNumber() {
        return this.bigDecimalNumber;
    }

    /**
     * Gets json node.
     *
     * @return the json node
     */
    public JsonNode getJsonNode() {
        return this.jsonNode;
    }

    /**
     * Gets simple enum.
     *
     * @return the simple enum
     */
    public SimpleEnum getSimpleEnum() {
        return this.simpleEnum;
    }

    /**
     * Gets number enum.
     *
     * @return the number enum
     */
    public NumberEnum getNumberEnum() {
        return this.numberEnum;
    }

    /**
     * Gets null data.
     *
     * @return the null data
     */
    public String getNullData() {
        return this.nullData;
    }

    /**
     * Gets address.
     *
     * @return the address
     */
    public AddressEntity getAddress() {
        return this.address;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s, %s, %s]", this.getFirstName(), this.getLastName(), this.title(), this.getBloodGroup());
    }
}
