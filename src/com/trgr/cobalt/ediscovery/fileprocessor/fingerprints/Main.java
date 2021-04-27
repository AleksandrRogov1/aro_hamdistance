/**
 * Copyright 2019: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */

package com.trgr.cobalt.ediscovery.fileprocessor.fingerprints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trgr.cobalt.ediscovery.fileprocessor.fingerprints.neardup.document.FingerprintCharikar;
import com.trgr.cobalt.ediscovery.fileprocessor.fingerprints.neardup.simhash.Simhash;
import com.trgr.cobalt.ediscovery.fileprocessor.fingerprints.utils.DiscoveryUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        if (args[0] != null && args[1] != null) {
            final ObjectMapper mapper = new ObjectMapper();
            try {
                final InputModel inputModel = mapper.readValue(Files.newInputStream(Paths.get(args[0])), InputModel.class);
                final List<NearDupModel> neardups = new ArrayList<>(inputModel.getDocuments().size());
                for (final DocumentModel docModel : inputModel.getDocuments()) {
                    NearDupModel nearDupModel = calculateFingerprints(inputModel, docModel);
                    calcHammDistances(neardups, nearDupModel);
                    neardups.add(nearDupModel);
                }

                final String outputFileName = args[1];
                mapper.writeValue(Files.newOutputStream(Paths.get(outputFileName)),  neardups);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private static void calcHammDistances(final List<NearDupModel> neardups, final NearDupModel nearDupModel) {
        for (final NearDupModel model : neardups) {
            final Distances distances = new Distances();
            distances.setDoc(model.getDocumentLocation());
            distances.setRabinDistance(calcHammDistance(nearDupModel.getRabinSim(), model.getRabinSim()));
            distances.setFnv1a64Distance(calcHammDistance(nearDupModel.getFnvSim(), model.getFnvSim()));
            distances.setMurmurDistance(calcHammDistance(nearDupModel.getMurmurSim(), model.getMurmurSim()));
            nearDupModel.getDistances().add(distances);

            final Distances distances2 = new Distances();
            distances2.setDoc(nearDupModel.getDocumentLocation());
            distances2.setRabinDistance(distances.getRabinDistance());
            distances2.setFnv1a64Distance(distances.getFnv1a64Distance());
            distances2.setMurmurDistance(distances.getMurmurDistance());
            model.getDistances().add(distances2);
        }
    }

    private static int calcHammDistance(final int[] sim1, final int[] sim2) {
        int dist = 0;
        for (int i =0; i < Simhash.LONGSIZE; i++) {
            if (sim1[i] != sim2[i]) {
                dist++;
            }
        }
        return dist;
    }

    private static NearDupModel calculateFingerprints(final InputModel inputModel, final DocumentModel documentModel) throws Exception {
        if (documentModel != null) {
            final File file = new File(documentModel.getDocumentLocation());
            if(file.exists() && file.length() > 100 && file.length() <= 250000000) {
                final FingerprintCharikar fingerprintDocument = new FingerprintCharikar(inputModel);

                String languageCode = documentModel.getLanguageCode();
                if (languageCode == null) {
                    languageCode = "en";
                }
                final Charset charset = DiscoveryUtils.detectCharset(documentModel.getDocumentLocation());


                return fingerprintDocument.calculateFingerprint(documentModel.getDocumentLocation(), languageCode, charset);
            }
        }
        return null;
    }
}
