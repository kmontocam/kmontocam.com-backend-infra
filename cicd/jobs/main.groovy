#!/usr/bin/env groovy

multibranchPipelineJob('kmontocam.com/backend') {
    branchSources {
        git {
            id('e9875f0d-d8dd-45c5-a871-eea9ca3e0118')
            description('Continuous integration for kmontocam.com backend')
            remote('https://github.com/kmontocam/kmontocam.com-backend.git')
        }
    }

    configure { node ->

        def triggersNode = node / 'triggers'

        triggersNode << 'com.cloudbees.hudson.plugins.folder.computed.PeriodicFolderTrigger' {
            spec('* * * * *')
            interval(300000)
        }

        triggersNode << disabled(false)

        def gitSCMSource = node / sources / 'data' / 'jenkins.branch.BranchSource' / source

        gitSCMSource << 'traits' {
            'jenkins.plugins.git.traits.BranchDiscoveryTrait' {}

            'jenkins.scm.impl.trait.WildcardSCMHeadFilterTrait' {
                includes('develop main')
                excludes('')
            }

            'jenkins.plugins.git.traits.CloneOptionTrait' {
                'extension' {
                    'hudson.plugins.git.extensions.impl.CloneOption' {
                        shallow(false)
                        noTags(false)
                    }
                }
            }

            'jenkins.plugins.git.traits.CleanAfterCheckoutTrait' {
                'extension' {
                    'hudson.plugins.git.extensions.impl.CleanCheckout' {}
                }
            }

            'jenkins.plugins.git.traits.CleanBeforeCheckoutTrait' {
                'extension' {
                    'hudson.plugins.git.extensions.impl.CleanBeforeCheckout' {}
                }
            }
        }
    }

    displayName 'backend'
}
