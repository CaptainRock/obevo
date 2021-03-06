/**
 * Copyright 2017 Goldman Sachs.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.gs.obevo.dbmetadata.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.gs.obevo.api.appdata.PhysicalSchema;
import com.gs.obevo.dbmetadata.api.DaDirectory;
import com.gs.obevo.dbmetadata.api.DaExtension;
import com.gs.obevo.dbmetadata.api.DaPackage;
import com.gs.obevo.dbmetadata.api.DaRoutine;
import com.gs.obevo.dbmetadata.api.DaRoutineType;
import com.gs.obevo.dbmetadata.api.DaRule;
import com.gs.obevo.dbmetadata.api.DaSchema;
import com.gs.obevo.dbmetadata.api.DaUserType;
import com.gs.obevo.dbmetadata.api.RuleBinding;
import org.eclipse.collections.api.collection.ImmutableCollection;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.ImmutableSet;
import schemacrawler.schema.Catalog;
import schemacrawler.schema.Schema;
import schemacrawler.schemacrawler.InformationSchemaKey;
import schemacrawler.schemacrawler.LimitOptionsBuilderFixed;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
import schemacrawler.schemacrawler.SchemaRetrievalOptionsBuilder;

public interface DbMetadataDialect {
    SchemaRetrievalOptionsBuilder getDbSpecificOptionsBuilder(Connection conn, PhysicalSchema physicalSchema, boolean searchAllTables) throws IOException;

    /**
     * Modifies the given LimitOptionsBuilder variable (i.e. which kinds of objects to fetch) with options specific to
     * this DB dialect.
     */
    default void updateLimitOptionsBuilder(LimitOptionsBuilderFixed options) {
    }

    /**
     * Modifies the given SchemaInfoLevelBuilder variable (i.e. the fields to fetch) with options specific to this DB
     * dialect.
     * @param schemaInfoLevelBuilder
     */
    default void updateSchemaInfoLevelBuilder(SchemaInfoLevelBuilder schemaInfoLevelBuilder) {
    }

    default MutableMap<InformationSchemaKey, String> getInfoSchemaSqlOverrides(PhysicalSchema physicalSchema) {
        return null;
    }

    /**
     * Sets the schema on the connection. This is needed prior to the schemacrawler calls for some DBMS types.
     */
    default void setSchemaOnConnection(Connection conn, PhysicalSchema physicalSchema) {
    }

    String getSchemaExpression(PhysicalSchema physicalSchema);

    String getTableExpression(PhysicalSchema physicalSchema, String tableName);

    String getRoutineExpression(PhysicalSchema physicalSchema, String procedureName);

    void validateDatabase(Catalog database, PhysicalSchema physicalSchema);

    ImmutableCollection<RuleBinding> getRuleBindings(DaSchema schema, Connection conn);

    ImmutableCollection<DaRoutine> searchExtraRoutines(DaSchema schema, String procedureName, Connection conn) throws SQLException;

    ImmutableCollection<DaPackage> searchPackages(DaSchema schema, String packageName, Connection conn) throws SQLException;

    ImmutableCollection<ExtraIndexInfo> searchExtraConstraintIndices(DaSchema schema, String tableName, Connection conn) throws SQLException;

    ImmutableCollection<ExtraRerunnableInfo> searchExtraViewInfo(DaSchema schema, String tableName, Connection conn) throws SQLException;

    ImmutableCollection<DaRule> searchRules(DaSchema schema, Connection conn) throws SQLException;

    ImmutableCollection<DaUserType> searchUserTypes(DaSchema schema, Connection conn) throws SQLException;

    /**
     * Overrides the routine type value in the {@link DaRoutine} if a value is returned. If null, then default to what
     * SchemaCrawler provides.
     */
    DaRoutineType getRoutineOverrideValue();

    /**
     * Indicates how we will read the desired catalog name from the SchemaCrawler {@link Schema} object.
     */
    SchemaStrategy getSchemaStrategy();

    /**
     * Retrieves the groups setup at the database level.
     * @since 6.6.0
     */
    ImmutableSet<String> getGroupNamesOptional(Connection conn, PhysicalSchema physicalSchema) throws SQLException;

    /**
     * Retrieves the users setup at the database level.
     * @since 6.6.0
     */
    ImmutableSet<String> getUserNamesOptional(Connection conn, PhysicalSchema physicalSchema) throws SQLException;

    /**
     * Retrieves the directory objects setup at the database level; pertinent for Oracle only.
     * @since 6.6.0
     */
    ImmutableSet<DaDirectory> getDirectoriesOptional(Connection conn) throws SQLException;

    /**
     * Retrieves the extension objects setup at the database level; pertinent for PostgreSQL only.
     * @since 7.0.0
     */
    ImmutableSet<DaExtension> getExtensionsOptional(Connection conn) throws SQLException;
}
