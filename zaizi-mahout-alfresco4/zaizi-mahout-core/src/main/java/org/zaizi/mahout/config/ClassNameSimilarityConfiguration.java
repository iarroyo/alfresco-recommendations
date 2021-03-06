/**
 * Alfresco Content Recommendation. Copyright (C) 2014 Zaizi Limited.
 *
 * ——————-
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 * ———————
 */
package org.zaizi.mahout.config;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.similarity.CachingItemSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.CachingUserSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.lang.reflect.Constructor;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrey
 * Date: 12/09/2011
 * Time: 12:38
 * To change this template use File | Settings | File Templates.
 */
public class ClassNameSimilarityConfiguration implements SimilarityConfiguration {

    private String similarityClassName;

    public String getSimilarityClassName() {
        return similarityClassName;
    }

    public void setSimilarityClassName(String similarityClassName) {
        this.similarityClassName = similarityClassName;
    }



    public ItemSimilarity getItemSimilarity(DataModel dataModel) throws TasteException {
        try {
            Class<ItemSimilarity> similarityClass =
                    (Class<ItemSimilarity>) getClass().getClassLoader().loadClass(getSimilarityClassName());
            Constructor<ItemSimilarity> ctor = similarityClass.getDeclaredConstructor(DataModel.class);
            return new CachingItemSimilarity(ctor.newInstance(dataModel), dataModel);
        } catch (Exception exc) {
            if (exc instanceof TasteException)
                throw (TasteException) exc;
            else
                throw new TasteException(exc);
        }
    }

    public UserSimilarity getUserSimilarity(DataModel dataModel) throws TasteException  {
        try {
            Class<UserSimilarity> similarityClass =
                    (Class<UserSimilarity>) getClass().getClassLoader().loadClass(getSimilarityClassName());
            Constructor<UserSimilarity> ctor = similarityClass.getDeclaredConstructor(DataModel.class);
            CachingUserSimilarity userSimilarity = new CachingUserSimilarity(ctor.newInstance(dataModel), dataModel);

            return userSimilarity;
        } catch (Exception exc) {
            if (exc instanceof TasteException)
                throw (TasteException) exc;
            else
                throw new TasteException(exc);
        }
    }

    @Override
    public String toString() {
        return new StringBuilder("ClassNameSimilarityConfiguration{").append(
                "similarityClassName='" ).append( similarityClassName + '\'' ).append(
                '}').toString();
    }
}
