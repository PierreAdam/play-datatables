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

package dataprovider;

import com.github.javafaker.Name;

/**
 * PersonEntity.
 *
 * @author Pierre Adam
 * @since 21.03.02
 */
public class PersonEntity {

    private final String firstName;

    private final String lastName;

    private final String title;

    private final String bloodGroup;

    /**
     * Instantiates a new Person entity.
     *
     * @param name the name
     */
    public PersonEntity(final Name name) {
        this.firstName = name.firstName();
        this.lastName = name.lastName();
        this.title = name.title();
        this.bloodGroup = name.bloodGroup();
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
    public String getTitle() {
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

    @Override
    public String toString() {
        return String.format("[%s, %s, %s, %s", this.getFirstName(), this.getLastName(), this.getTitle(), this.getBloodGroup());
    }
}
