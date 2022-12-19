package su.codes.elasticsearch.repositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import su.codes.elasticsearch.constants.ESConstants;
import su.codes.elasticsearch.models.Info;

import java.io.IOException;
import java.util.List;

@Component
public class InfoRepository {

    private static final String SUCCESSFUL_CREATE = "Info document created successfully with ID : %s";
    private static final String SUCCESSFUL_UPDATE = "Info document updated successfully for the ID : %s";
    private static final String FAILURE_CREATE_UPDATE = "Error occurred while performing the operation.";
    private static final String FAILURE_BULK = "Error occurred while performing bulk operation : %s";

    @Value("${elasticsearch.index.info:index_name}")
    private String indexName;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public String createOrUpdateInfo(Info info) throws IOException {
        IndexResponse response = elasticsearchClient.index(
                i -> i.index(indexName)
                        .id(info.getId())
                        .document(info)
        );
        if (response.result().name().equals(ESConstants.CREATED)) {
            return String.format(SUCCESSFUL_CREATE, response.id());
        } else if (response.result().name().equals(ESConstants.UPDATED)) {
            return String.format(SUCCESSFUL_UPDATE, response.id());
        }
        return FAILURE_CREATE_UPDATE;
    }

    public Info getInfoById(String infoId) throws IOException {
        Info info = null;
        GetResponse<Info> response = elasticsearchClient.get(
                getRequest -> getRequest.index(indexName)
                        .id(infoId),
                Info.class
        );
        if (response.found()) {
            info = response.source();
            info.setId(response.id());
        }
        return info;
    }

    public String bulkCreateOrUpdateInfo(List<Info> infoList) throws IOException {
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (Info info : infoList) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index(indexName)
                            .id(info.getId())
                            .document(info)
                    )
            );
        }

        BulkResponse result = elasticsearchClient.bulk(br.build());

        if (result.errors()) {
            for (BulkResponseItem item : result.items()) {
                if (item.error() != null) {
                    return String.format(FAILURE_BULK, item.error());
                }
            }
        }

        return result.toString();
    }

}
