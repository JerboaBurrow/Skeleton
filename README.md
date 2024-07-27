- ```gpg --full-generate-key```
- ```gpg --output private.pgp --armor --export-secret-key email```

Set GPG_PASS and GPG_KEY in actions secrets

Generate a keystore for the Android app, ```keys.jks```

- ```gpg --recipient GPG_ID --armor --encrypt keys.jks```

Set as KEYSTORE secret along with its PASS and KEY_ALIAS. This is so the
keystore file can be stored as a string value in actions for automated signing.