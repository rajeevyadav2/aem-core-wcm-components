
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2018 Adobe
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~     http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
package com.adobe.cq.wcm.core.components.internal.services.table;


import com.adobe.cq.wcm.core.components.services.table.ResourceReader;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.dam.commons.util.DamUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.osgi.service.component.annotations.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.nonNull;

@Component(service = ResourceReader.class, immediate = true)
public class ResourceReaderImpl implements ResourceReader {

    @OSGiService
    private ResourceResolver resourceResolver;

    private List<List<String>> rows;

    @Override
    public List<List<String>> readData(Resource resource, String[] propertyNames) throws IOException {
        rows = new ArrayList<>();
        if (DamUtil.isAsset(resource)) {
            processCSVData(resource, propertyNames);
        } else processResourceData(resource, propertyNames);

        return rows;
    }

    private void processResourceData(Resource resource, String[] propertyNames) {

        Iterator<Resource> children = resourceResolver.listChildren(resource);
        while (children.hasNext()) {
            Resource child = children.next();
            ValueMap props = child.adaptTo(ValueMap.class);
            List<String> row = new ArrayList<>();
            for (String propertyName : propertyNames) {
                String propValue = props != null ? props.get(propertyName, "") : "";
                row.add(propValue);
            }
            rows.add(row);
        }
    }

    private void processCSVData(Resource resource, String[] propertyNames) throws IOException {
        Asset asset = DamUtil.resolveToAsset(resource);
        if (nonNull(asset)) {
            Rendition original = asset.getOriginal();
            if (nonNull(original)) {
                InputStream inputStream = original.getStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                List<String[]> lines = getRawCSVDataInList(br);

                final String[] columns = lines.get(0);
                Map<String, Integer> columnIndexMap = getColumnIndexMap(columns);
                //get the column indexes for matching property name
                Map<String, Integer> tableColumnsMap = new HashMap<>();
                for (String propertyName : propertyNames) {
                    tableColumnsMap.put(propertyName, columnIndexMap.getOrDefault(propertyName, -1));
                }
                //remove header row from the list
                lines = lines.subList(1, lines.size() - 1);
                populateRowsForTable(lines, tableColumnsMap);
                br.close();

            }
        }
    }

    private void populateRowsForTable(List<String[]> lines, Map<String, Integer> finalMap) {
        List<String> row = new ArrayList<>();
        for (String[] line : lines) {
            finalMap.forEach((k, v) ->
                {
                    if (v >= 0) {
                        row.add(line[v]);
                    } else row.add("");
                }
            );
            rows.add(row);
        }
    }

    private List<String[]> getRawCSVDataInList(BufferedReader br) throws IOException {
        String[][] csvData;
        List<String[]> lines = new ArrayList<>();
        String currentLine;
        while ((currentLine = br.readLine()) != null) {
            lines.add(currentLine.split(","));
        }
        csvData = new String[lines.size()][0];
        lines.toArray(csvData);
        return lines;
    }

    /**
     * @param columns : Array of Column names. This is first row in the CSV file
     * @return : Returns the column name and it's index
     */
    private Map<String, Integer> getColumnIndexMap(String[] columns) {
        return IntStream.range(0, columns.length)
            .boxed()
            .collect(Collectors.toMap(index -> columns[index], index -> index, (a, b) -> b));
    }


}
