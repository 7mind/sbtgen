# sbtgen

Use

```bash
sbt +clean +publishLocalSigned 
ant -f .publish.xml deploy
```

instead of

```bash
sbt +clean +publishSigned +sonatypeRelease
```

