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

import com.github.javafaker.Address;
import com.github.javafaker.Faker;

import java.util.UUID;

/**
 * AddressEntity.
 *
 * @author Pierre Adam
 * @since 21.04.01
 */
public class AddressEntity {

    /**
     * The Uid.
     */
    private final UUID uid;

    /**
     * The Street address.
     */
    private final String streetAddress;

    /**
     * The City.
     */
    private final String city;

    /**
     * The Zip code.
     */
    private final String zipCode;

    /**
     * Instantiates a new Address entity.
     */
    AddressEntity(final Faker faker) {
        final Address address = faker.address();
        this.uid = UUID.randomUUID();
        this.streetAddress = address.streetAddress(true);
        this.city = address.city();
        this.zipCode = address.zipCode();
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
     * Gets street address.
     *
     * @return the street address
     */
    public String getStreetAddress() {
        return this.streetAddress;
    }

    /**
     * Gets city.
     *
     * @return the city
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Gets zip code.
     *
     * @return the zip code
     */
    public String getZipCode() {
        return this.zipCode;
    }
}
