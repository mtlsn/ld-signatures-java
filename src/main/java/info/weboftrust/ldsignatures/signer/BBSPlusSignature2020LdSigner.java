package info.weboftrust.ldsignatures.signer;

import bbs.signatures.KeyPair;
import com.danubetech.keyformats.crypto.ByteSigner;
import com.danubetech.keyformats.crypto.impl.BLS12381_G2_BBSPlus_PrivateKeySigner;
import com.danubetech.keyformats.jose.JWSAlgorithm;
import info.weboftrust.ldsignatures.LdProof;
import info.weboftrust.ldsignatures.jsonld.LDSecurityContexts;
import info.weboftrust.ldsignatures.suites.BBSPlusSignature2020SignatureSuite;
import info.weboftrust.ldsignatures.suites.SignatureSuites;
import io.ipfs.multibase.Multibase;

import java.security.GeneralSecurityException;

public class BBSPlusSignature2020LdSigner extends LdSigner<BBSPlusSignature2020SignatureSuite> {

    public BBSPlusSignature2020LdSigner(ByteSigner signer) {

        super(SignatureSuites.SIGNATURE_SUITE_BBSPLUSSIGNATURE2020, signer);
    }

    public BBSPlusSignature2020LdSigner(KeyPair privateKey) {

        this(new BLS12381_G2_BBSPlus_PrivateKeySigner(privateKey));
    }

    public BBSPlusSignature2020LdSigner() {

        this((ByteSigner) null);
    }

    public static void sign(LdProof.Builder ldProofBuilder, byte[] signingInput, ByteSigner signer) throws GeneralSecurityException {

        // sign

        String proofValue;

        byte[] bytes = signer.sign(signingInput, JWSAlgorithm.BBSPlus);
        proofValue = Multibase.encode(Multibase.Base.Base58BTC, bytes);

        // add JSON-LD context

        ldProofBuilder.context(LDSecurityContexts.JSONLD_CONTEXT_W3ID_SECURITY_BBS_V1);

        // done

        ldProofBuilder.proofValue(proofValue);
    }

    @Override
    public void sign(LdProof.Builder ldProofBuilder, byte[] signingInput) throws GeneralSecurityException {

        sign(ldProofBuilder, signingInput, this.getSigner());
    }
}
