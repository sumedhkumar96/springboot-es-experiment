package su.codes.elasticsearch.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import su.codes.elasticsearch.models.Info;
import su.codes.elasticsearch.repositories.InfoRepository;

import java.io.IOException;
import java.util.List;

@Service
public class InfoService {

    @Autowired
    private InfoRepository infoRepository;

    public String addInfo(Info info) throws IOException {
        return infoRepository.createOrUpdateInfo(info);
    }

    public Info getInfo(String id) throws IOException {
        return infoRepository.getInfoById(id);
    }

    public String addMultipleInfoRecords(List<Info> infoList) throws IOException {
        return infoRepository.bulkCreateOrUpdateInfo(infoList);
    }
}
