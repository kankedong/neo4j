/*
 * Copyright (c) 2002-2016 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.kernel.api.impl.schema.populator;

import java.io.IOException;

import org.neo4j.kernel.api.impl.schema.LuceneSchemaIndex;
import org.neo4j.kernel.api.impl.schema.writer.LuceneIndexWriter;
import org.neo4j.kernel.api.index.IndexPopulator;

public abstract class LuceneIndexPopulator implements IndexPopulator
{
    protected LuceneSchemaIndex luceneIndex;
    protected LuceneIndexWriter writer;

    LuceneIndexPopulator( LuceneSchemaIndex luceneIndex )
    {
        this.luceneIndex = luceneIndex;
    }

    @Override
    public void create() throws IOException
    {
        luceneIndex.create();
        luceneIndex.open();
        writer = luceneIndex.getIndexWriter();
    }

    @Override
    public void drop() throws IOException
    {
        luceneIndex.drop();
    }

    @Override
    public void close( boolean populationCompletedSuccessfully ) throws IOException
    {
        try
        {
            if ( populationCompletedSuccessfully )
            {
                flush();
                luceneIndex.markAsOnline();
            }
        }
        finally
        {
            luceneIndex.close();
        }
    }

    @Override
    public void markAsFailed( String failure ) throws IOException
    {
        luceneIndex.markAsFailed( failure );
    }

    protected abstract void flush() throws IOException;
}
