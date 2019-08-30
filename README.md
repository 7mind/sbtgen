# sbtgen

Use

```bash
source ./.secrets/ant-secrets.sh
mkdir tasks
curl -o tasks/nexus-staging-ant-tasks-1.6.3-uber.jar https://search.maven.org/remotecontent\?filepath\=org/sonatype/nexus/ant/nexus-staging-ant-tasks/1.6.3/nexus-staging-ant-tasks-1.6.3-uber.jar
sbt +clean +publishSigned 
ant -f .publish.xml deploy
```

instead of

```bash
sbt +clean +publishSigned +sonatypeRelease
```

